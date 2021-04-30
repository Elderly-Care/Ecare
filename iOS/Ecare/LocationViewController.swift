//
//  LocationViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 10/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase
class LocationViewController: UIViewController {

    var ref: DatabaseReference!
    @IBOutlet weak var locationLabel: UILabel!
    
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        ref = Database.database().reference()
        getStatus()
        // Do any additional setup after loading the view.
    }
    

    func getStatus(){
//        if i dont want to store everything in a dictionary and access bathroom 3alatool w sa3tha snapshot.value! hayb2a shayel data el bathroom w no lets b2a el variables.
//        self.ref.child("26uHKXQw5yZOgC8RZ6yoOCcHBmq1/Bathroom")
//26uHKXQw5yZOgC8RZ6yoOCcHBmq1 : User ID of the person you want to get the data of
        self.ref.child("TzV04aBQlhTn8CN7w07Ub1p9k7G2").child("Location").observe(DataEventType.value, with: {(snapshot) in
            if snapshot.exists() {
                let valueSnap = snapshot.value as? NSDictionary
                let bathroom = valueSnap?["Bathroom"] as? NSDictionary
                let bathroomvalue = bathroom?["Value"] as? Int ?? 0
                let kitchen = valueSnap?["Kitchen"] as? NSDictionary
                let kitchenvalue = kitchen?["Value"] as? Int ?? 0

//                let kitchen = valueSnap?["Kitchen"] as? Int ?? 0
  
                print("Got data \(snapshot.value!)")
//                print(bathroom)
                if kitchenvalue == 0 && bathroomvalue == 1 {
                self.locationLabel.text = "Bathroom"
                }
                else if kitchenvalue == 1 && bathroomvalue == 0  {
                   self.locationLabel.text = "Kitchen"
                }
                else {
//                    unknown location of receiving Both
                    self.locationLabel.text = "Unknown Location"
                }
            }
            else {
                print("No data available")
                
            }
        })
        
        
    }
    @IBAction func locationHhistory(_ sender: UIButton) {
        
        self.performSegue(withIdentifier: "locHistory", sender: self)
        
    }
    

}
