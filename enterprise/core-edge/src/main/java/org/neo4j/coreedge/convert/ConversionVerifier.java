/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.coreedge.convert;

import org.neo4j.kernel.impl.store.StoreId;

public class ConversionVerifier
{
    public void conversionGuard( ClusterSeed source,
                                 StoreMetadata target )
    {
        StoreId sourceBefore = source.before();
        StoreId targetBefore = target.storeId();

        long sourceLastTxId = source.lastTxId();
        long targetLastTxId = target.lastTxId();

        if ( !(sourceBefore.theRealEquals( targetBefore ) && sourceLastTxId == targetLastTxId) )
        {
            throw new RuntimeException(
                    String.format( "Cannot convert store. Backup ID does not match on source and target databases. Source: %s, Target: %s", source, target ) );
        }
    }
}
