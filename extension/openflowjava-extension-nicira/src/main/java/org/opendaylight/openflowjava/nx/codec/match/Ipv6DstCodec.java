/*
 * Copyright (c) 2017 Red Hat, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowjava.nx.codec.match;

import static org.opendaylight.yangtools.yang.common.netty.ByteBufUtils.readUint32;

import io.netty.buffer.ByteBuf;
import org.opendaylight.openflowjava.protocol.api.keys.MatchEntryDeserializerKey;
import org.opendaylight.openflowjava.protocol.api.keys.MatchEntrySerializerKey;
import org.opendaylight.openflowjava.protocol.api.util.EncodeConstants;
import org.opendaylight.openflowjava.protocol.api.util.OxmMatchConstants;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.MatchField;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.Nxm1Class;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.OxmClassBase;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.match.entries.grouping.MatchEntry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.NxmOfIpDst;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.ofj.aug.nx.match.IpDstCaseValue;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.ofj.aug.nx.match.IpDstCaseValueBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflowjava.nx.match.rev140421.ofj.nxm.of.match.ip.dst.grouping.IpDstValuesBuilder;

/**
 * Codec for the Ipv6Dst message.
 *
 * @author Sridhar Gaddam (sgaddam@redhat.com)
 */
public class Ipv6DstCodec extends AbstractMatchCodec {

    private static final int VALUE_LENGTH = 16;
    private static final int NXM_FIELD_CODE = 20;
    public static final MatchEntrySerializerKey<Nxm1Class, NxmOfIpDst> SERIALIZER_KEY = new MatchEntrySerializerKey<>(
            EncodeConstants.OF_VERSION_1_3, Nxm1Class.class, NxmOfIpDst.class);
    public static final MatchEntryDeserializerKey DESERIALIZER_KEY = new MatchEntryDeserializerKey(
            EncodeConstants.OF_VERSION_1_3, OxmMatchConstants.NXM_1_CLASS, NXM_FIELD_CODE);


    @Override
    public MatchEntry deserialize(final ByteBuf message) {
        return deserializeHeaderToBuilder(message)
                .setMatchEntryValue(new IpDstCaseValueBuilder()
                    .setIpDstValues(new IpDstValuesBuilder().setValue(readUint32(message)).build())
                    .build())
                .build();
    }

    @Override
    public void serialize(final MatchEntry input, final ByteBuf outBuffer) {
        serializeHeader(input, outBuffer);
        IpDstCaseValue ipDstCase = (IpDstCaseValue) input.getMatchEntryValue();
        outBuffer.writeInt(ipDstCase.getIpDstValues().getValue().intValue());
    }

    @Override
    public int getNxmFieldCode() {
        return NXM_FIELD_CODE;
    }

    @Override
    public int getOxmClassCode() {
        return OxmMatchConstants.NXM_1_CLASS;
    }

    @Override
    public int getValueLength() {
        return VALUE_LENGTH;
    }

    @Override
    public Class<? extends MatchField> getNxmField() {
        return NxmOfIpDst.class;
    }

    @Override
    public Class<? extends OxmClassBase> getOxmClass() {
        return Nxm1Class.class;
    }

}
