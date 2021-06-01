/*
 * Copyright (c) 2015 Hewlett-Packard Enterprise and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowjava.nx.codec.match;

import io.netty.buffer.ByteBuf;
import org.opendaylight.openflowjava.protocol.api.keys.MatchEntryDeserializerKey;
import org.opendaylight.openflowjava.protocol.api.keys.MatchEntrySerializerKey;
import org.opendaylight.openflowjava.protocol.api.util.EncodeConstants;
import org.opendaylight.openflowjava.protocol.api.util.OxmMatchConstants;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.PortNumber;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.MatchField;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.Nxm0Class;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.OxmClassBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.match.entries.grouping.MatchEntry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.NxmOfTcpDst;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.ofj.aug.nx.match.TcpDstCaseValue;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.ofj.aug.nx.match.TcpDstCaseValueBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.ofj.nxm.of.match.tcp.dst.grouping.TcpDstValuesBuilder;
import org.opendaylight.yangtools.yang.common.netty.ByteBufUtils;

/**
 * Codec for the TcpDst message.
 *
 * @author Aswin Suryanarayanan.
 */
public class TcpDstCodec extends AbstractMatchCodec {

    private static final int VALUE_LENGTH = 2;
    private static final int NXM_FIELD_CODE = 10;
    public static final MatchEntrySerializerKey<Nxm0Class, NxmOfTcpDst> SERIALIZER_KEY = new MatchEntrySerializerKey<>(
            EncodeConstants.OF_VERSION_1_3, Nxm0Class.class, NxmOfTcpDst.class);
    public static final MatchEntryDeserializerKey DESERIALIZER_KEY = new MatchEntryDeserializerKey(
            EncodeConstants.OF_VERSION_1_3, OxmMatchConstants.NXM_0_CLASS, NXM_FIELD_CODE);

    @Override
    public void serialize(final MatchEntry input, final ByteBuf outBuffer) {
        serializeHeader(input, outBuffer);
        TcpDstCaseValue tcpDstCase = (TcpDstCaseValue) input.getMatchEntryValue();
        outBuffer.writeShort(tcpDstCase.getTcpDstValues().getPort().getValue().toJava());
        outBuffer.writeShort(tcpDstCase.getTcpDstValues().getMask().toJava());
    }

    @Override
    public MatchEntry deserialize(final ByteBuf message) {
        return deserializeHeaderToBuilder(message)
                .setHasMask(true)
                .setMatchEntryValue(new TcpDstCaseValueBuilder()
                    .setTcpDstValues(new TcpDstValuesBuilder()
                        .setPort(new PortNumber(ByteBufUtils.readUint16(message)))
                        .setMask(ByteBufUtils.readUint16(message))
                        .build())
                    .build())
                .build();
    }

    @Override
    public int getNxmFieldCode() {
        return NXM_FIELD_CODE;
    }

    @Override
    public int getOxmClassCode() {
        return OxmMatchConstants.NXM_0_CLASS;
    }

    @Override
    public int getValueLength() {
        return VALUE_LENGTH;
    }

    @Override
    public Class<? extends MatchField> getNxmField() {
        return NxmOfTcpDst.class;
    }

    @Override
    public Class<? extends OxmClassBase> getOxmClass() {
        return Nxm0Class.class;
    }
}
