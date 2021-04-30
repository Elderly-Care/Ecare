//
//  PatientStatusViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 10/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Charts
import Firebase


class PatientStatusViewController: UIViewController, ChartViewDelegate {
    
    let db = Firestore.firestore()
    var barChart = BarChartView()
    
    var hr: NSDictionary = NSDictionary()
    var hrEntries : [Int] = []
    var dayEntries : [String] = []
    
    var steps: NSDictionary = NSDictionary()
    var stepEntries : [Int] = []
    var stepdayEntries : [String] = []

    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        barChart.xAxis.valueFormatter = self
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



            for x in 0..<hrEntries.count{
            entries.append(BarChartDataEntry(x: Double(x), y: Double(hrEntries[x])))
        }



        let set = BarChartDataSet(entries: entries)
            set.colors = ChartColorTemplates.joyful()
        let data = BarChartData(dataSet: set)
        barChart.data = data
        super.viewDidLayoutSubviews()
        view.addSubview(barChart)
    }
    
    
    
    
    @IBAction func checkLocation(_ sender: UIButton) {
        self.performSegue(withIdentifier: "checkLocation", sender: self)
    }
    
    
    
    @IBAction func viewHealthData(_ sender: UIButton) {
        
        let userID = (Auth.auth().currentUser?.uid)!
        let docRef = db.collection("users").document(userID)
              
              docRef.getDocument { (document, error) in
                  if let document = document, document.exists {
                      let dataDescription = document.data()
                      
                      self.steps = (dataDescription?["weekSteps"] as? NSDictionary)!
                      //                print("Document data: \(hr["Friday"]!)")
                      //                print(self.hr.count)
                      let HRCount = self.steps.count
                      //                print(self.hr )
                      for (key, value) in self.steps {
                          //                    print("Value: \(value) for key: \(key)")
                          self.stepEntries.append(value as! Int)
                          self.stepdayEntries.append(key as! String)
                          
                      }
                      
                      self.performSegue(withIdentifier: "healthData", sender: self)
                    
                      
                  }
                  else
                  {
                      print("Document does not exist")
                  }
              }
        
    }
    
    @IBAction func healthReports(_ sender: UIButton) {
                              self.performSegue(withIdentifier: "report", sender: self)
    }
    
    
    
    
    
    
    
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "healthData" {
            let destinationVC = segue.destination as! HealthDataViewController
            destinationVC.stepdayEntries = stepdayEntries
            destinationVC.stepEntries = stepEntries
            
        }
    }
    
    
    
    
    
}
extension PatientStatusViewController: IAxisValueFormatter{
    func stringForValue(_ value: Double, axis: AxisBase?) -> String {
        let months = dayEntries
        return months[Int(value)]
    }
    
    
}
