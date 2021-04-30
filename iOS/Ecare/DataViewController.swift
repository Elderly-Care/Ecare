//
//  DataViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 23/03/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class DataViewController: UIViewController {
    
    @IBOutlet weak var table: UITableView!
    
    let db = Firestore.firestore()
    @IBOutlet weak var which: UILabel!
    var heartRate: NSDictionary = NSDictionary()
    var hrEntries : [Int] = []
    var dayEntries : [String] = []
    var steps: NSDictionary = NSDictionary()
    var stepEntries : [Int] = []
    var stepdayEntries : [String] = []
    var whichOne = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        table.delegate = self
        table.dataSource = self
        print(whichOne)
        showData()
        
        // Do any additional setup after loading the view.
    }
    
    func showData(){
        if whichOne == "heart"{
            which.text = "Heart Rate"
            
            
//            let userID = (Auth.auth().currentUser?.uid)!
            let userID = "TzV04aBQlhTn8CN7w07Ub1p9k7G2"
            let docRef = db.collection("history").document(userID)
            
            docRef.getDocument { (document, error) in
                if let document = document, document.exists {
                    let dataDescription = document.data()
                    
                    self.heartRate = (dataDescription?["heartRate"] as? NSDictionary)!
                    for (key, value) in self.heartRate {

                        self.hrEntries.append(value as! Int)
                        self.dayEntries.append(key as! String)
                        DispatchQueue.main.async {
                            self.table.reloadData()
                            let indexPath = IndexPath(row: self.dayEntries.count-1 , section: 0)
                        }
                        
                    }
                    
                    
                    
                }
                else
                {
                    print("Document does not exist")
                }
            }
        }
        else if whichOne == "steps"{
            which.text = "Steps"
            
            let userID = "TzV04aBQlhTn8CN7w07Ub1p9k7G2"
            let docRef = db.collection("history").document(userID)
            
            docRef.getDocument { (document, error) in
                if let document = document, document.exists {
                    let dataDescription = document.data()
                    
                    self.heartRate = (dataDescription?["steps"] as? NSDictionary)!

                    for (key, value) in self.heartRate {
                        self.hrEntries.append(value as! Int)
                        self.dayEntries.append(key as! String)
             
                        
                        DispatchQueue.main.async {
                            self.table.reloadData()
                            let indexPath = IndexPath(row: self.dayEntries.count-1 , section: 0)
                        }
                    }
                    
                    
                    
                }
                else
                {
                    print("Document does not exist")
                }
            }
            
            
        }
        else if whichOne == "calories"{
            which.text = "Calories"
            
            let userID = "TzV04aBQlhTn8CN7w07Ub1p9k7G2"
            let docRef = db.collection("history").document(userID)
            
            docRef.getDocument { (document, error) in
                if let document = document, document.exists {
                    let dataDescription = document.data()
                    
                    self.heartRate = (dataDescription?["calories"] as? NSDictionary)!

                    for (key, value) in self.heartRate {
                        self.hrEntries.append(value as! Int)
                        self.dayEntries.append(key as! String)
             
                        
                        DispatchQueue.main.async {
                            self.table.reloadData()
                            let indexPath = IndexPath(row: self.dayEntries.count-1 , section: 0)
                        }
                    }
                    
                    
                    
                }
                else
                {
                    print("Document does not exist")
                }
            }
        }
        else if whichOne == "sleep"{
            which.text = "Sleep"
            
            let userID = "TzV04aBQlhTn8CN7w07Ub1p9k7G2"
            let docRef = db.collection("history").document(userID)
            
            docRef.getDocument { (document, error) in
                if let document = document, document.exists {
                    let dataDescription = document.data()
                    
                    self.heartRate = (dataDescription?["sleep"] as? NSDictionary)!

                    for (key, value) in self.heartRate {
                        let hrs = (value as! Int) / 60
                        self.hrEntries.append(hrs)
                        self.dayEntries.append(key as! String)
             
                        
                        DispatchQueue.main.async {
                            self.table.reloadData()
                            let indexPath = IndexPath(row: self.dayEntries.count-1 , section: 0)
                        }
                    }
                    
                    
                    
                }
                else
                {
                    print("Document does not exist")
                }
            }
        }
        
        
        
    }
    
    
    /*x
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    
}
extension DataViewController: UITableViewDataSource , UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dayEntries.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = self.table.dequeueReusableCell(withIdentifier: "cell" , for: indexPath)
        
        cell.textLabel?.text = dayEntries[indexPath.row] + ":           " + String(hrEntries[indexPath.row] )
        return cell
        
    }
    
    
}
