/*
 * Copyright (c) 2024 PANTHEON.tech, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.services.sal;

import static java.util.Objects.requireNonNull;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.stream.Collectors;
import org.opendaylight.openflowplugin.impl.util.BarrierUtil;
import org.opendaylight.openflowplugin.impl.util.GroupUtil;
import org.opendaylight.openflowplugin.impl.util.PathUtil;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.transaction.rev150304.SendBarrier;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.service.rev130918.RemoveGroup;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.service.rev130918.RemoveGroupInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.Group;
import org.opendaylight.yang.gen.v1.urn.opendaylight.group.types.rev131018.GroupRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.groups.service.rev160315.RemoveGroupsBatch;
import org.opendaylight.yang.gen.v1.urn.opendaylight.groups.service.rev160315.RemoveGroupsBatchInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.groups.service.rev160315.RemoveGroupsBatchOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRef;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RemoveGroupsBatchImpl implements RemoveGroupsBatch {
    private static final Logger LOG = LoggerFactory.getLogger(RemoveGroupsBatchImpl.class);

    private final RemoveGroup removeGroup;
    private final SendBarrier sendBarrier;

    public RemoveGroupsBatchImpl(final RemoveGroup removeGroup, final SendBarrier sendBarrier) {
        this.removeGroup = requireNonNull(removeGroup);
        this.sendBarrier = requireNonNull(sendBarrier);
    }

    @Override
    public ListenableFuture<RpcResult<RemoveGroupsBatchOutput>> invoke(final RemoveGroupsBatchInput input) {
        final var groups = input.nonnullBatchRemoveGroups().values();
        if (LOG.isTraceEnabled()) {
            LOG.trace("Removing groups @ {} : {}", PathUtil.extractNodeId(input.getNode()), groups.size());
        }

        final var resultsLot = groups.stream()
            .map(addGroup -> removeGroup.invoke(new RemoveGroupInputBuilder(addGroup)
                .setGroupRef(createGroupRef(input.getNode(), addGroup))
                .setNode(input.getNode())
                .build()))
            .collect(Collectors.toList());

        final var commonResult = Futures.transform(Futures.allAsList(resultsLot),
            GroupUtil.createCumulatingFunction(groups), MoreExecutors.directExecutor());

        final var removeGroupsBulkFuture = Futures.transform(commonResult, GroupUtil.GROUP_REMOVE_TRANSFORM,
            MoreExecutors.directExecutor());

        return input.getBarrierAfter()
            ? BarrierUtil.chainBarrier(removeGroupsBulkFuture, input.getNode(), sendBarrier,
                GroupUtil.GROUP_REMOVE_COMPOSING_TRANSFORM)
            : removeGroupsBulkFuture;
    }

    private static GroupRef createGroupRef(final NodeRef nodeRef, final Group batchGroup) {
        return GroupUtil.buildGroupPath((InstanceIdentifier<Node>) nodeRef.getValue(), batchGroup.getGroupId());
    }
}
