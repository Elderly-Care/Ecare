//
//  SearchProfileViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 16/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class SearchProfileViewController: UIViewController {

    let db = Firestore.firestore()
    var uid = ""
    
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var addPatient: UIButton!
    @IBOutlet weak var emailLabel: UILabel!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadProfile()
        // Do any additional setup after loading the view.
    }
    
    func loadProfile(){
    let docRef = db.collection("users").document(uid)

    docRef.getDocument { (document, error) in
        if let document = document, document.exists {
            let dataDescription = document.data()
//            print("Document data: \((dataDescription?["name"])!)")
            let name = (dataDescription?["name"] as? String)!
            let email = (dataDescription?["email"] as? String)!
            self.nameLabel.text = name
            self.emailLabel.text = email
            
        } else {
            print("Document does not exist")
        }
    }
        }
    

    @IBAction func addPatient(_ sender: UIButton) {
        let myUID = Auth.auth().currentUser?.uid
        
        self.db.collection("users").document(uid).collection("requests").document(myUID!).setData([ "requestUid": myUID!, "status": 0
                                                                                                   
                    ]){
                        (error) in if let e=error {
                            print("There was an issue saving to Firestore , \(e)" )
                        }
                        else {
                            print("Saved successfully to firestore" )

                            self.addPatient.titleLabel?.text = "       Sent"
   
                            
        //                    DispatchQueue.main.async {
        //                        self.messageTextfield.text = ""
        //                    }
                        }
                    }
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
