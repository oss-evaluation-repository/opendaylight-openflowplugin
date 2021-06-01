/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.openflow.md.core.sal.convertor.common;

import static java.util.Objects.requireNonNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import org.opendaylight.yangtools.yang.common.Uint8;

/**
 * Injection lookup key based on version and target object.
 */
public class ConvertorKey {

    private final Uint8 version;
    private final Class<?> targetClazz;

    /**
     * Constructor.
     *
     * @param version openflow version
     * @param targetClazz target class
     */
    public ConvertorKey(final Uint8 version, final Class<?> targetClazz) {
        this.version = requireNonNull(version);
        this.targetClazz = requireNonNull(targetClazz);
    }

    @Override
    public int hashCode() {
        return 31 * version.hashCode() + targetClazz.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConvertorKey other = (ConvertorKey) obj;
        return version.equals(other.version) && targetClazz.equals(other.targetClazz);
    }

    @Override
    public final String toString() {
        return addToStringAttributes(MoreObjects.toStringHelper(this)).toString();
    }

    protected ToStringHelper addToStringAttributes(final ToStringHelper toStringHelper) {
        return toStringHelper.add("version", version).add("targetClazz", targetClazz);
    }
}
