//
//  PatientSignUpViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 12/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class PatientSignUpViewController: UIViewController {
    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var passwordTextfield: UITextField!
    @IBOutlet weak var heightTextfield: UITextField!
    @IBOutlet weak var weightTextfield: UITextField!
    @IBOutlet weak var nameTextfield: UITextField!
    @IBOutlet weak var ageTextfield: UITextField!
    @IBOutlet weak var phoneTextfield: UITextField!
    @IBOutlet weak var gender: UITextField!
    @IBOutlet weak var healthConditionTextfield: UITextField!
    
    let db = Firestore.firestore()
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func register(_ sender: UIButton) {
        if let email=emailTextfield.text , let password = passwordTextfield.text{ //if for optional binding
        Auth.auth().createUser(withEmail: email, password: password) { authResult, error in
            if let e = error{
                print(e.localizedDescription)
            }
            else {
                //Navigate to ChatViewController through segue that is created in the storyboard
                self.performSegue(withIdentifier: "patientRegister", sender: self)
                if let name = self.nameTextfield.text , let userID = Auth.auth().currentUser?.uid, let userPhone = self.phoneTextfield.text, let userEmail = Auth.auth().currentUser?.email, let userWeight = self.weightTextfield.text, let userHeight = self.heightTextfield.text, let userAge = self.ageTextfield.text, let userGender = self.gender.text , let userHealth = self.healthConditionTextfield.text  {
                    self.db.collection("users").document(userID).setData([ "uid": userID,"email": userEmail,"name": name, "date": Date(), "phone": userPhone,  "registerType": "Patient", "weight": userWeight, "height": userHeight, "age": userAge , "gender": userGender , "healthCondition": userHealth
                            ]){
                                (error) in if let e=error {
                                    print("There was an issue saving to Firestore , \(e)" )
                                }
                                else {
                                    print("Saved successfully to firestore" )
                //                    DispatchQueue.main.async {
                //                        self.messageTextfield.text = ""
                //                    }
                                }
                            }
                        }
            }
            }
        }

        
        
        
    }
    


}
