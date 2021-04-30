//
//  CaregiverEmergencyViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 30/03/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class CaregiverEmergencyViewController: UIViewController {
    @IBOutlet weak var sentTick: UILabel!
    @IBOutlet weak var name: UILabel!
    var userName = ""
    var ID = ""
    let db = Firestore.firestore()
    @IBOutlet weak var textField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        name.text = userName
        
        self.sentTick.isHidden = true
        // Do any additional setup after loading the view.
    }
    
    @IBAction func sendPressed(_ sender: UIButton) {
            var senderName = ""
            let userID = Auth.auth().currentUser?.uid
            let docRef = db.collection("users").document(userID!)
            
            docRef.getDocument { (document, error) in
                if let document = document, document.exists {
                    let dataDescription = document.data()
                    //        print("Document data: \((dataDescription?["name"])!)")
                    senderName = (dataDescription?["name"] as? String)!
                    
                    if let messageBody = self.textField.text , let messageSender = userID {
                        self.db.collection("users").document("TzV04aBQlhTn8CN7w07Ub1p9k7G2").collection("posts").addDocument(data: [
                            "uidSender": messageSender,"message": messageBody , "nameSender": senderName, "nameReceiver": self.userName , "date": Date()
                        ]){
                            (error) in if let e=error {
                                print("There was an issue saving post to Firestore , \(e)" )
                            }
                            else {
                                print("Saved successfully posts to firestore" )
                            }
                        }
                    }
                    self.textField.text = ""
                    
                    
                }
                else {
                    print("Document does not exist")
                }
                
            }
            
            
        }
        
        
    
    @IBAction func presetButton1(_ sender: UIButton) {
        var senderName = ""
        let userID = Auth.auth().currentUser?.uid
        let docRef = db.collection("users").document(userID!)
        
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                let dataDescription = document.data()
                //        print("Document data: \((dataDescription?["name"])!)")
                senderName = (dataDescription?["name"] as? String)!
                
                let messageBody = "I'm Better"
                let messageSender = userID
                    self.db.collection("users").document("TzV04aBQlhTn8CN7w07Ub1p9k7G2").collection("posts").addDocument(data: [
                        "uidSender": messageSender,"message": messageBody , "nameSender": senderName, "nameReceiver": self.userName , "date": Date()
                    ]){
                        (error) in if let e=error {
                            print("There was an issue saving post to Firestore , \(e)" )
                        }
                        else {
                            print("Saved successfully posts to firestore" )
                            self.sentTick.isHidden = false
                            
                            
                        }
                    }
                
                self.textField.text = ""
                
                
                
            }
            else {
                print("Document does not exist")
            }
           
        }
        
        
        
        
    }
    @IBAction func presetButton2(_ sender: UIButton) {
        var senderName = ""
        let userID = Auth.auth().currentUser?.uid
        let docRef = db.collection("users").document(userID!)
        
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                let dataDescription = document.data()
                //        print("Document data: \((dataDescription?["name"])!)")
                senderName = (dataDescription?["name"] as? String)!
                
                 let messageBody = "I Took My Medicine "
                let messageSender = userID
                    self.db.collection("users").document("TzV04aBQlhTn8CN7w07Ub1p9k7G2").collection("posts").addDocument(data: [
                        "uidSender": messageSender,"message": messageBody , "nameSender": senderName, "nameReceiver": self.userName , "date": Date()
                    ]){
                        (error) in if let e=error {
                            print("There was an issue saving post to Firestore , \(e)" )
                        }
                        else {
                            print("Saved successfully posts to firestore" )
                        }
                    }
                
                self.textField.text = ""
                
                
            }
            else {
                print("Document does not exist")
            }
            
        }
        
        
    }
    @IBAction func presetButton3(_ sender: UIButton) {
        
        var senderName = ""
               let userID = Auth.auth().currentUser?.uid
               let docRef = db.collection("users").document(userID!)
               
               docRef.getDocument { (document, error) in
                   if let document = document, document.exists {
                       let dataDescription = document.data()
                       //        print("Document data: \((dataDescription?["name"])!)")
                       senderName = (dataDescription?["name"] as? String)!
                       
                        let messageBody = "I'm Tired!"
                       let messageSender = userID
                           self.db.collection("users").document("TzV04aBQlhTn8CN7w07Ub1p9k7G2").collection("posts").addDocument(data: [
                               "uidSender": messageSender,"message": messageBody , "nameSender": senderName, "nameReceiver": self.userName , "date": Date()
                           ]){
                               (error) in if let e=error {
                                   print("There was an issue saving post to Firestore , \(e)" )
                               }
                               else {
                                   print("Saved successfully posts to firestore" )
                               }
                           }
                       
                       self.textField.text = ""
                       
                       
                   }
                   else {
                       print("Document does not exist")
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
    

