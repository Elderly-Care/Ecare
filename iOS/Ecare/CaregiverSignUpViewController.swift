//
//  SignUpViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 08/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class CaregiverSignUpViewController: UIViewController {

    @IBOutlet weak var nameTextfield: UITextField!
    @IBOutlet weak var passwordTextfield: UITextField!
    @IBOutlet weak var emailTextfield: UITextField!
    @IBOutlet weak var phoneTextfield: UITextField!
    @IBOutlet weak var relationTextfield: UITextField!
    
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
                self.performSegue(withIdentifier: "caregiverRegister", sender: self)
                if let name = self.nameTextfield.text , let userID = Auth.auth().currentUser?.uid, let userPhone = self.phoneTextfield.text, let userEmail = Auth.auth().currentUser?.email , let userRelation = self.relationTextfield.text  {
                    self.db.collection("users").document(userID).setData([ "uid": userID,"email": userEmail,"name": name, "date": Date(), "phone": userPhone,  "registerType": "Caregiver" , "relation": userRelation
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
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
