/*
 * Copyright (c) 2002-2018 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.graphdb.factory.module;

import java.io.File;
import java.util.function.Supplier;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.NeoStoreDataSource;
import org.neo4j.kernel.api.InwardKernel;
import org.neo4j.kernel.impl.core.TokenHolders;
import org.neo4j.kernel.impl.factory.GraphDatabaseFacade;
import org.neo4j.kernel.impl.proc.Procedures;
import org.neo4j.kernel.impl.store.StoreId;

public class DataSourceModule
{
    public final NeoStoreDataSource neoStoreDataSource;

    public final Supplier<InwardKernel> kernelAPI;

    public final Supplier<StoreId> storeId;

    public final TokenHolders tokenHolders;

    public DataSourceModule( String databaseName, final PlatformModule platformModule, EditionModule editionModule, Procedures procedures,
            GraphDatabaseFacade graphDatabaseFacade )
    {

        tokenHolders = editionModule.tokenHoldersSupplier.get();
        File databaseDirectory = new File( platformModule.storeDir, databaseName );

        platformModule.diagnosticsManager.prependProvider( platformModule.config );

        neoStoreDataSource = new NeoStoreDataSource( new ModularDatabaseCreationContext( databaseName, databaseDirectory, platformModule, editionModule,
                procedures, graphDatabaseFacade, tokenHolders ) );

        this.storeId = neoStoreDataSource::getStoreId;
        this.kernelAPI = neoStoreDataSource::getKernel;

        ProcedureGDSFactory gdsFactory = new ProcedureGDSFactory( platformModule, this, editionModule.coreAPIAvailabilityGuard, tokenHolders );
        procedures.registerComponent( GraphDatabaseService.class, gdsFactory::apply, true );
    }
}
