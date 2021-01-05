/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowjava.protocol.impl.deserialization.action;

import static org.opendaylight.yangtools.yang.common.netty.ByteBufUtils.readUint8;

import io.netty.buffer.ByteBuf;
import org.opendaylight.openflowjava.protocol.impl.util.ActionConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.action.grouping.action.choice.SetNwTtlCase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.action.grouping.action.choice.SetNwTtlCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.action.grouping.action.choice.set.nw.ttl._case.SetNwTtlActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.actions.grouping.Action;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.actions.grouping.ActionBuilder;

/**
 * OF13SetNwTtlActionDeserializer.
 *
 * @author michal.polkorab
 */
public class OF13SetNwTtlActionDeserializer extends AbstractActionDeserializer<SetNwTtlCase> {
    public OF13SetNwTtlActionDeserializer() {
        super(new SetNwTtlCaseBuilder().build());
    }

    @Override
    public Action deserialize(final ByteBuf input) {
        input.skipBytes(2 * Short.BYTES);
        final var ttl = readUint8(input);
        input.skipBytes(ActionConstants.SET_NW_TTL_PADDING);

        return new ActionBuilder()
            .setActionChoice(new SetNwTtlCaseBuilder()
                .setSetNwTtlAction(new SetNwTtlActionBuilder().setNwTtl(ttl).build())
                .build())
            .build();
    }
}
