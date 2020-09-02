package org.globalforestwatch.layers

import org.globalforestwatch.grids.GridTile

case class GrossEmissionsNonCo2Co2e(gridTile: GridTile, model: String="standard")
  extends FloatLayer
    with OptionalFLayer {
      val model_suffix = if (model == "standard") "" else s"__$model"
  val uri: String =
//    s"$basePath/gfw_gross_emissions_co2e_non_co2$model_suffix/v20191106/raster/epsg-4326/${gridTile.gridSize}/${gridTile.rowCount}/Mg_ha-1/geotiff/${gridTile.tileId}.tif"
    s"s3://gfw-files/flux_2_1_0/gross_emissions_non_co2_co2e/standard/${gridTile.tileId}.tif"
}
