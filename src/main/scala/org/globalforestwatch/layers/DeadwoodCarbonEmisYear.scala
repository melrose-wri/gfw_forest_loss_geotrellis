package org.globalforestwatch.layers

import org.globalforestwatch.grids.GridTile

case class DeadwoodCarbonEmisYear(gridTile: GridTile, model: String="standard")
  extends FloatLayer
    with OptionalFLayer {
      val model_suffix = if (model == "standard") "" else s"__$model"
  val uri: String =
//    s"$basePath/gfw_deadwood_carbon_stock_in_emissions_year$model_suffix/v20191106/raster/epsg-4326/${gridTile.gridSize}/${gridTile.rowCount}/Mg_ha-1/geotiff/${gridTile.tileId}.tif"
    s"s3://gfw-files/flux_2_1_0/deadwood_emis_year/standard/${gridTile.tileId}.tif"
}
