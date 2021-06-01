/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.statistics.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.opendaylight.openflowplugin.api.OFConstants;
import org.opendaylight.openflowplugin.api.openflow.device.DeviceContext;
import org.opendaylight.openflowplugin.api.openflow.device.RequestContextStack;
import org.opendaylight.openflowplugin.api.openflow.device.Xid;
import org.opendaylight.openflowplugin.impl.services.util.RequestInputUtils;
import org.opendaylight.openflowplugin.impl.statistics.services.compatibility.AbstractCompatibleStatService;
import org.opendaylight.openflowplugin.impl.statistics.services.compatibility.FlowStatisticsToNotificationTransformer;
import org.opendaylight.openflowplugin.openflow.md.core.sal.convertor.ConvertorExecutor;
import org.opendaylight.openflowplugin.openflow.md.core.sal.convertor.data.VersionConvertorData;
import org.opendaylight.openflowplugin.openflow.md.core.sal.convertor.match.MatchInjector;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.statistics.rev130819.FlowsStatisticsUpdate;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.statistics.rev130819.GetFlowStatisticsFromFlowTableInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.statistics.rev130819.GetFlowStatisticsFromFlowTableOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.statistics.rev130819.GetFlowStatisticsFromFlowTableOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.transaction.rev150304.TransactionId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MultipartType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.MultipartReply;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.MultipartRequestInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.OfHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.MultipartRequestFlowCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.multipart.request.flow._case.MultipartRequestFlowBuilder;
import org.opendaylight.yangtools.yang.common.Uint32;

public final class FlowsInTableService extends AbstractCompatibleStatService<GetFlowStatisticsFromFlowTableInput,
        GetFlowStatisticsFromFlowTableOutput, FlowsStatisticsUpdate> {

    private final ConvertorExecutor convertorExecutor;
    private final VersionConvertorData data;

    public FlowsInTableService(final RequestContextStack requestContextStack,
                               final DeviceContext deviceContext,
                               final AtomicLong compatibilityXidSeed,
                               final ConvertorExecutor convertorExecutor) {
        super(requestContextStack, deviceContext, compatibilityXidSeed);
        this.convertorExecutor = convertorExecutor;
        this.data = new VersionConvertorData(getVersion());
    }

    @Override
    protected OfHeader buildRequest(final Xid xid,
                                    final GetFlowStatisticsFromFlowTableInput input) {
        final MultipartRequestFlowCaseBuilder multipartRequestFlowCaseBuilder = new MultipartRequestFlowCaseBuilder();
        final MultipartRequestFlowBuilder mprFlowRequestBuilder = new MultipartRequestFlowBuilder();

        if (input.getTableId() != null) {
            mprFlowRequestBuilder.setTableId(input.getTableId());
        } else {
            mprFlowRequestBuilder.setTableId(OFConstants.OFPTT_ALL);
        }

        if (input.getOutPort() != null) {
            mprFlowRequestBuilder.setOutPort(Uint32.valueOf(input.getOutPort().longValue()));
        } else {
            mprFlowRequestBuilder.setOutPort(OFConstants.OFPP_ANY);
        }

        if (input.getOutGroup() != null) {
            mprFlowRequestBuilder.setOutGroup(input.getOutGroup());
        } else {
            mprFlowRequestBuilder.setOutGroup(OFConstants.OFPG_ANY);
        }

        if (input.getCookie() != null) {
            mprFlowRequestBuilder.setCookie(input.getCookie().getValue());
        } else {
            mprFlowRequestBuilder.setCookie(OFConstants.DEFAULT_COOKIE);
        }

        if (input.getCookieMask() != null) {
            mprFlowRequestBuilder.setCookieMask(input.getCookieMask().getValue());
        } else {
            mprFlowRequestBuilder.setCookieMask(OFConstants.DEFAULT_COOKIE_MASK);
        }

        // convert and inject match
        final Optional<Object> conversionMatch = convertorExecutor.convert(input.getMatch(), data);
        MatchInjector.inject(conversionMatch, mprFlowRequestBuilder, data.getVersion());

        // Set request body to main multipart request
        multipartRequestFlowCaseBuilder.setMultipartRequestFlow(mprFlowRequestBuilder.build());
        final MultipartRequestInputBuilder mprInput = RequestInputUtils.createMultipartHeader(
                MultipartType.OFPMPFLOW, xid.getValue(), getVersion());
        mprInput.setMultipartRequestBody(multipartRequestFlowCaseBuilder.build());

        return mprInput.build();
    }

    @Override
    public GetFlowStatisticsFromFlowTableOutput buildTxCapableResult(final TransactionId emulatedTxId) {
        return new GetFlowStatisticsFromFlowTableOutputBuilder().setTransactionId(emulatedTxId).build();
    }

    @Override
    public FlowsStatisticsUpdate transformToNotification(final List<MultipartReply> result,
            final TransactionId emulatedTxId) {
        return FlowStatisticsToNotificationTransformer.transformToNotification(result,
                                                                               getDeviceInfo(),
                                                                               getOfVersion(),
                                                                               emulatedTxId,
                                                                               convertorExecutor);
    }
}
