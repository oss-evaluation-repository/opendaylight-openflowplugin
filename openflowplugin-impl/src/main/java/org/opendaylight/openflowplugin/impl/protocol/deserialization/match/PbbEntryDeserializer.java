/*
 * Copyright (c) 2016 Pantheon Technologies s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.protocol.deserialization.match;

import io.netty.buffer.ByteBuf;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.MatchBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.match.ProtocolMatchFieldsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.model.match.types.rev131026.protocol.match.fields.PbbBuilder;
import org.opendaylight.yangtools.yang.common.Uint32;

public class PbbEntryDeserializer extends AbstractMatchEntryDeserializer {

    @Override
    public void deserializeEntry(final ByteBuf message, final MatchBuilder builder) {
        final boolean hasMask = processHeader(message);
        final long pbb = message.readUnsignedMedium();
        final PbbBuilder pbbBuilder = new PbbBuilder()
            .setPbbIsid(Uint32.valueOf(pbb));

        if (hasMask) {
            pbbBuilder.setPbbMask(Uint32.valueOf(message.readUnsignedMedium()));
        }

        if (builder.getProtocolMatchFields() == null) {
            builder.setProtocolMatchFields(new ProtocolMatchFieldsBuilder()
                    .setPbb(pbbBuilder.build())
                    .build());
        } else if (builder.getProtocolMatchFields().getPbb() == null) {
            builder.setProtocolMatchFields(new ProtocolMatchFieldsBuilder(builder.getProtocolMatchFields())
                    .setPbb(pbbBuilder.build())
                    .build());
        } else {
            throwErrorOnMalformed(builder, "protocolMatchFields", "pbb");
        }
    }
}
