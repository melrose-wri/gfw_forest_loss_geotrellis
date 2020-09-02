package org.globalforestwatch.layers

import org.globalforestwatch.grids.GridTile

case class DeadwoodCarbon2000(gridTile: GridTile, model: String="standard")
  extends FloatLayer
    with OptionalFLayer {
  val model_suffix: String = if (model == "standard") "" else s"__$model"
  val uri: String =
  //  s"$basePath/gfw_deadwood_carbon_stock_2000$model_suffix/v20191106/raster/epsg-4326/${gridTile.gridSize}/${gridTile.rowCount}/Mg_ha-1/geotiff/${gridTile.tileId}.tif"
    s"s3://gfw-files/flux_2_1_0/deadwood_carbon_2000/standard/${gridTile.tileId}.tif"
}