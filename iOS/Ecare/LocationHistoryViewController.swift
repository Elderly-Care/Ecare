//
//  LocationHistoryViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 04/04/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class LocationHistoryViewController: UIViewController {
    @IBOutlet weak var whichLabel: UILabel!
    let db = Firestore.firestore()
    var which = ""
    @IBOutlet weak var tableView: UITableView!
    var bathroomHistory: [LocationHistory] = []
    override func viewDidLoad() {
        tableView.delegate = self
        tableView.dataSource = self
        if which == "bathroom"{
            whichLabel.text = "Bathroom"
        }
        else {
             whichLabel.text = "Kitchen"
            
        }
        super.viewDidLoad()
        loadHistory()
        // Do any additional setup after loading the view.
       
        
        
        
    }
    
    func loadHistory(){
        if self.which == "bathroom"{
            
        let userID = "TzV04aBQlhTn8CN7w07Ub1p9k7G2"
        let docRef = db.collection("history").document(userID)
        
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                let dataDescription = (document.data() as NSDictionary?)!
                let bathroomData = (dataDescription["Bathroom"] as? NSDictionary)!
//                print(dataDescription["Bathroom"])
                
//                self.heartRate = (dataDescription?["heartRate"] as? NSDictionary)!
                for (key, value) in bathroomData {
//                    print(key)
//                    print("------VALUES")
                    let values = (value as? NSDictionary)!
                    let numberofEntries = values["Num_Entries"] as? Int
                    let totalDurations = values["Total_Time"] as? Int
                    let newHistory = LocationHistory(Date: key as? String ?? "NA", Num_Entries: numberofEntries ?? 0, Duration: totalDurations ?? 0)
                    self.bathroomHistory.append(newHistory)
//                    self.dateBathroom.append(value as! Int)
//                    self.date.append(key as! String)
                    DispatchQueue.main.async {
                        self.tableView.reloadData()
                        let indexPath = IndexPath(row: self.bathroomHistory.count-1 , section: 0)
                    }

                }
                print(self.bathroomHistory[0].Date)

                print(self.bathroomHistory.count)
                
                
                
            }
            else
            {
                print("Document does not exist")
            }
        }
        
        
    }
        else {
                let userID = "TzV04aBQlhTn8CN7w07Ub1p9k7G2"
                let docRef = db.collection("history").document(userID)
                
                docRef.getDocument { (document, error) in
                    if let document = document, document.exists {
                        let dataDescription = (document.data() as NSDictionary?)!
                        let bathroomData = (dataDescription["Kitchen"] as? NSDictionary)!
        //                print(dataDescription["Bathroom"])
                        
        //                self.heartRate = (dataDescription?["heartRate"] as? NSDictionary)!
                        for (key, value) in bathroomData {
        //                    print(key)
        //                    print("------VALUES")
                            let values = (value as? NSDictionary)!
                            let numberofEntries = values["Num_Entries"] as? Int
                            let totalDurations = values["Total_Time"] as? Int
                            let newHistory = LocationHistory(Date: key as? String ?? "NA", Num_Entries: numberofEntries ?? 0, Duration: totalDurations ?? 0)
                            self.bathroomHistory.append(newHistory)
        //                    self.dateBathroom.append(value as! Int)
        //                    self.date.append(key as! String)
                            DispatchQueue.main.async {
                                self.tableView.reloadData()
                                let indexPath = IndexPath(row: self.bathroomHistory.count-1 , section: 0)
                            }

                        }
                        print(self.bathroomHistory[0].Date)

                        print(self.bathroomHistory.count)
                        
                        
                        
                    }
                    else
                    {
                        print("Document does not exist")
                    }
                }
                
        }
    }

    
}
extension LocationHistoryViewController: UITableViewDataSource , UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return bathroomHistory.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = self.tableView.dequeueReusableCell(withIdentifier: "cell" , for: indexPath)
        let hist = bathroomHistory[indexPath.row]
        //        cell.name.text = requestNames[indexPath.row]
        let st1 = hist.Date
        let st2 = String(hist.Num_Entries)
        let st3 = String(hist.Duration)
        let theLabel = st1 + "            " + st2 + "                       " + st3
        cell.textLabel?.text = theLabel
        

        
        return cell
        
    }

    
}
