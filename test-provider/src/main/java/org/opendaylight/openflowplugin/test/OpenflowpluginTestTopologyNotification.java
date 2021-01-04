/*
 * Copyright (c) 2014, 2015 Ericsson India Global Services Pvt Ltd. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.test;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.opendaylight.mdsal.binding.api.NotificationService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.FlowTopologyDiscoveryListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.LinkDiscovered;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.LinkOverutilized;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.LinkRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.topology.discovery.rev130819.LinkUtilizationNormal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenflowpluginTestTopologyNotification {

    private static final Logger LOG = LoggerFactory.getLogger(OpenflowpluginTestTopologyNotification.class);

    private final TopologyEventListener topologyEventListener = new TopologyEventListener();
    private final NotificationService notificationService;

    public OpenflowpluginTestTopologyNotification(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void init() {
        // For switch events
        notificationService.registerNotificationListener(topologyEventListener);
    }

    private static final class TopologyEventListener implements FlowTopologyDiscoveryListener {
        @Override
        @Deprecated
        @SuppressFBWarnings("SLF4J_SIGN_ONLY_FORMAT")
        public void onLinkDiscovered(final LinkDiscovered notification) {
            LOG.debug("-------------------------------------------");
            LOG.debug("LinkDiscovered notification ........");
            LOG.debug("-------------------------------------------");
        }

        @Override
        @Deprecated
        @SuppressFBWarnings("SLF4J_SIGN_ONLY_FORMAT")
        public void onLinkOverutilized(final LinkOverutilized notification) {
            LOG.debug("-------------------------------------------");
            LOG.debug("LinkOverutilized notification ........");
            LOG.debug("-------------------------------------------");
        }

        @Override
        @Deprecated
        @SuppressFBWarnings("SLF4J_SIGN_ONLY_FORMAT")
        public void onLinkRemoved(final LinkRemoved notification) {
            LOG.debug("-------------------------------------------");
            LOG.debug("LinkRemoved notification   ........");
            LOG.debug("-------------------------------------------");
        }

        @Override
        @Deprecated
        @SuppressFBWarnings("SLF4J_SIGN_ONLY_FORMAT")
        public void onLinkUtilizationNormal(final LinkUtilizationNormal notification) {
            LOG.debug("-------------------------------------------");
            LOG.debug("LinkUtilizationNormal notification ........");
            LOG.debug("-------------------------------------------");
        }

    }
}
