/*
 * Copyright (c) 2016 Hewlett-Packard Enterprise and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowjava.nx.codec.action;

import io.netty.buffer.ByteBuf;
import org.opendaylight.openflowjava.nx.api.NiciraActionDeserializerKey;
import org.opendaylight.openflowjava.nx.api.NiciraActionSerializerKey;
import org.opendaylight.openflowjava.nx.api.NiciraConstants;
import org.opendaylight.openflowjava.protocol.api.util.EncodeConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.actions.grouping.Action;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.action.rev150203.actions.grouping.ActionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.ExperimenterId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.action.rev140421.ofj.aug.nx.action.ActionLearn;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.action.rev140421.ofj.aug.nx.action.ActionLearnBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.action.rev140421.ofj.nx.action.learn.grouping.NxActionLearnBuilder;

/**
 * Codec for the learn action.
 *
 * @author Slava Radune
 */
public class LearnCodec extends AbstractActionCodec {
    public static final byte NXAST_LEARN_SUBTYPE = 16;
    public static final NiciraActionSerializerKey SERIALIZER_KEY =
            new NiciraActionSerializerKey(EncodeConstants.OF_VERSION_1_3, ActionLearn.class);
    public static final NiciraActionDeserializerKey DESERIALIZER_KEY =
            new NiciraActionDeserializerKey(EncodeConstants.OF_VERSION_1_3, NXAST_LEARN_SUBTYPE);
    private static final int MUL_LENGTH = 8;

    @Override
    public void serialize(final Action input, final ByteBuf outBuffer) {
        final ActionLearn action = (ActionLearn) input.getActionChoice();
        final int length = LearnCodecUtil.calcLength(action);
        int lengthMod = length % MUL_LENGTH;
        if (lengthMod != 0) {
            lengthMod = MUL_LENGTH - lengthMod;
        }
        serializeHeader(length + lengthMod, NXAST_LEARN_SUBTYPE, outBuffer);

        LearnCodecUtil.serializeLearnHeader(outBuffer, action);
        LearnCodecUtil.serializeFlowMods(outBuffer, action);

        //pad with zeros for the length to be multiplication by 8
        if (lengthMod != 0) {
            outBuffer.writeZero(lengthMod);
        }
    }

    @Override
    public Action deserialize(final ByteBuf message) {

        short length = LearnCodecUtil.deserializeHeader(message);

        final var nxActionLearnBuilder = new NxActionLearnBuilder();
        LearnCodecUtil.deserializeLearnHeader(message, nxActionLearnBuilder);

        length -= LearnCodecUtil.HEADER_LENGTH;

        LearnCodecUtil.buildFlowModSpecs(nxActionLearnBuilder, message, length);

        return new ActionBuilder()
            .setExperimenterId(new ExperimenterId(NiciraConstants.NX_VENDOR_ID))
            .setActionChoice(new ActionLearnBuilder()
                .setNxActionLearn(nxActionLearnBuilder.build())
                .build())
            .build();
    }
}
