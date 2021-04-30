//
//  HealthDataViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 23/03/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Charts
import Firebase

class HealthDataViewController: UIViewController, ChartViewDelegate {
    var barChart = BarChartView()
    var stepEntries : [Int] = []
    var stepdayEntries : [String] = []
    
       override func viewDidLoad() {
            super.viewDidLoad()
//            barChart.xAxis.valueFormatter = self
            barChart.delegate = self
    //        print(dayEntries)
    //        print(hrEntries)
            // Do any additional setup after loading the view.
        }
        
        
        

            
            override func viewDidLayoutSubviews() {
            
                barChart.frame = CGRect(x:0 , y:0 , width: self.view.frame.size.width ,
                                        height: self.view.frame.size.width )
            barChart.center = view.center
            var entries = [BarChartDataEntry]()



                for x in 0..<stepEntries.count{
                entries.append(BarChartDataEntry(x: Double(x), y: Double(stepEntries[x])))
            }



            let set = BarChartDataSet(entries: entries)
                set.colors = ChartColorTemplates.joyful()
            let data = BarChartData(dataSet: set)
            barChart.data = data
            super.viewDidLayoutSubviews()
            view.addSubview(barChart)
        }


    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
