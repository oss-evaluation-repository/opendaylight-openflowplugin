/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Ipv4Address;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.MacAddress;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.FlowWildcardsV10;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.oxm.rev150225.match.v10.grouping.MatchV10Builder;
import org.opendaylight.yangtools.yang.common.Uint16;
import org.opendaylight.yangtools.yang.common.Uint8;

public class MatchUtilTest {

    private static final MacAddress ZERO_MAC_ADDRESS = new MacAddress("00:00:00:00:00:00");
    private static final Ipv4Address ZERO_IPV4_ADDRESS = new Ipv4Address("0.0.0.0");

    @Test
    public void createEmptyV10MatchTest() {
        MatchV10Builder expectedV10Match = expectedV10Match();
        MatchV10Builder emptyV10Match = MatchUtil.createEmptyV10Match();
        assertEquals(expectedV10Match.build(),emptyV10Match.build());
    }

    private static MatchV10Builder expectedV10Match() {
        MatchV10Builder matchV10Builder = new MatchV10Builder();
        matchV10Builder.setDlDst(ZERO_MAC_ADDRESS);
        matchV10Builder.setDlSrc(ZERO_MAC_ADDRESS);
        matchV10Builder.setDlType(Uint16.ZERO);
        matchV10Builder.setDlVlan(Uint16.ZERO);
        matchV10Builder.setDlVlanPcp(Uint8.ZERO);
        matchV10Builder.setInPort(Uint16.ZERO);
        matchV10Builder.setNwDst(ZERO_IPV4_ADDRESS);
        matchV10Builder.setNwDstMask(Uint8.ZERO);
        matchV10Builder.setNwProto(Uint8.ZERO);
        matchV10Builder.setNwSrc(ZERO_IPV4_ADDRESS);
        matchV10Builder.setNwSrcMask(Uint8.ZERO);
        matchV10Builder.setNwTos(Uint8.ZERO);
        matchV10Builder.setTpDst(Uint16.ZERO);
        matchV10Builder.setTpSrc(Uint16.ZERO);
        FlowWildcardsV10 flowWildcardsV10 =
                new FlowWildcardsV10(true, true, true, true, true, true, true, true, true, true);
        matchV10Builder.setWildcards(flowWildcardsV10);
        return matchV10Builder;
    }
}
