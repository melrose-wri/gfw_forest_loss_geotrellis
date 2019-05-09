package org.globalforestwatch.gladalerts

import java.time.LocalDate
import geotrellis.contrib.polygonal.CellVisitor
import geotrellis.raster._
import cats.implicits._
import org.globalforestwatch.util.Geodesy
import geotrellis.raster.histogram.StreamingHistogram

/** LossData Summary by year */
case class GladAlertsSummary(
  stats: Map[GladAlertsDataGroup, GladAlertsData] = Map.empty
) {

  /** Combine two Maps and combine their LossData when a year is present in both */
  def merge(other: GladAlertsSummary): GladAlertsSummary = {
    // the years.combine method uses LossData.lossDataSemigroup instance to perform per value combine on the map
    GladAlertsSummary(stats.combine(other.stats))
  }
}

object GladAlertsSummary {
  // TreeLossSummary form Raster[TreeLossTile] -- cell types may not be the same

  implicit val mdhCellRegisterForTreeLossRaster1
    : CellVisitor[Raster[GladAlertsTile], GladAlertsSummary] =
    new CellVisitor[Raster[GladAlertsTile], GladAlertsSummary] {

      def register(raster: Raster[GladAlertsTile],
                   col: Int,
                   row: Int,
                   acc: GladAlertsSummary): GladAlertsSummary = {
        // This is a pixel by pixel operation
        val glad: (LocalDate, Boolean) = raster.tile.glad.getData(col, row)

        if (glad != null) {
          val biomass: Double = raster.tile.biomass.getData(col, row)
          val climateMask: Boolean = raster.tile.climateMask.getData(col, row)

          val cols: Int = raster.rasterExtent.cols
          val rows: Int = raster.rasterExtent.rows
          val ext = raster.rasterExtent.extent
          val cellSize = raster.cellSize

          val lat: Double = raster.rasterExtent.gridRowToMap(row)
          val lon: Double = raster.rasterExtent.gridColToMap(col)
          val area: Double = Geodesy.pixelArea(lat, raster.cellSize) // uses Pixel's center coordiate.  +- raster.cellSize.height/2 doesn't make much of a difference

          val areaHa = area / 10000.0
          val biomassPixel = biomass * areaHa
          val co2Pixel = ((biomass * areaHa) * 0.5) * 44 / 12

          val pKey = GladAlertsDataGroup(lat, lon, climateMask)

          val summary: GladAlertsData =
            acc.stats.getOrElse(key = pKey, default = GladAlertsData(0, 0, 0))

          summary.totalArea += areaHa
          summary.totalBiomass += biomassPixel
          summary.totalCo2 += co2Pixel

          val updated_summary: Map[GladAlertsDataGroup, GladAlertsData] =
            acc.stats.updated(pKey, summary)

          GladAlertsSummary(updated_summary)
        }
      }
    }
}
