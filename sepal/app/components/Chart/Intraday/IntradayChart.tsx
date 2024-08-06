import styles from './IntradayChart.module.scss'
import React from "react";
import Chart from 'react-apexcharts'

/**
 * Intraday chart using apex charts
 *
 * @param series data
 * @param options config
 * @param id id
 * @author Stephen Prizio
 * @version 0.0.1
 */
export function IntradayChart({series = [], options = {}, id = 0}: {series: any, options: any, id: number}) {

  const baseClass = "intraday-chart"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div id={'chart' + id}>
        <Chart options={options} series={series} type="candlestick" height={500} />
      </div>
    </div>
  )
}