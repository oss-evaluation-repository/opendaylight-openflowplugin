/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.karaf;

import org.apache.karaf.shell.api.action.Action;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Reference;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.Session;
import org.opendaylight.openflowplugin.api.openflow.statistics.ofpspecific.MessageIntelligenceAgency;

@Command(scope = "ofp", name = "showStats", description = "Show openflow statistics.")
@Service
public class ShowStatsCommandProvider implements Action {
    @Reference
    Session session;
    @Reference
    MessageIntelligenceAgency messageIntelligenceAgency;

    @Override
    public Object execute() {
        final var console = session.getConsole();
        messageIntelligenceAgency.provideIntelligence().forEach(console::println);
        return null;
    }
}
