/*
 * Copyright (c) 2013 Pantheon Technologies s.r.o. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowjava.protocol.impl.serialization.action;

import org.opendaylight.openflowjava.protocol.impl.util.ActionConstants;

/**
 * Serializes OF 1.3 DecNwTtl actions.
 *
 * @author michal.polkorab
 */
public class OF13DecNwTtlActionSerializer extends AbstractEmptyActionSerializer {
    public OF13DecNwTtlActionSerializer() {
        super(ActionConstants.DEC_NW_TTL_CODE);
    }
}
