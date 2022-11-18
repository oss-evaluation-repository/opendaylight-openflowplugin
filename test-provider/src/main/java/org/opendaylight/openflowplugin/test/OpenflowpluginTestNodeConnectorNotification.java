/*
 * Copyright (c) 2014, 2015 Ericsson India Global Services Pvt Ltd. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.opendaylight.mdsal.binding.api.NotificationService;
import org.opendaylight.mdsal.binding.api.NotificationService.CompositeListener;
import org.opendaylight.mdsal.binding.api.NotificationService.CompositeListener.Component;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeConnectorUpdated;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeRemoved;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.NodeUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenflowpluginTestNodeConnectorNotification {

    private static final Logger LOG = LoggerFactory.getLogger(OpenflowpluginTestNodeConnectorNotification.class);

    private final PortEventListener portEventListener = new PortEventListener();
    private final NotificationService notificationService;

    public OpenflowpluginTestNodeConnectorNotification(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void init() {
        // For switch events
        notificationService.registerCompositeListener(portEventListener.toListener());
    }

    private static final class PortEventListener {
        List<NodeUpdated> nodeUpdated = new ArrayList<>();
        List<NodeRemoved> nodeRemoved = new ArrayList<>();
        List<NodeConnectorUpdated> nodeConnectorUpdated = new ArrayList<>();
        List<NodeConnectorRemoved> nodeConnectorRemoved = new ArrayList<>();

        CompositeListener toListener() {
            return new CompositeListener(Set.of(
                new Component<>(NodeConnectorRemoved.class, notification -> {
                    LOG.debug("NodeConnectorRemoved Notification");
                    LOG.debug("NodeConnectorRef {}", notification.getNodeConnectorRef());
                    nodeConnectorRemoved.add(notification);
                }),
                new Component<>(NodeConnectorUpdated.class, notification -> {
                    LOG.debug("NodeConnectorUpdated Notification");
                    LOG.debug("NodeConnectorRef {}", notification.getNodeConnectorRef());
                    nodeConnectorUpdated.add(notification);
                }),
                new Component<>(NodeRemoved.class, notification -> {
                    LOG.debug("NodeRemoved Notification");
                    LOG.debug("NodeRef {}", notification.getNodeRef());
                    nodeRemoved.add(notification);
                }),
                new Component<>(NodeUpdated.class, notification -> {
                    LOG.debug("NodeUpdated Notification");
                    LOG.debug("NodeRef {}", notification.getNodeRef());
                    nodeUpdated.add(notification);
                })));
        }
    }
}
