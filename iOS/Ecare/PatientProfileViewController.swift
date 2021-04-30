//
//  PatientProfileViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 10/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class PatientProfileViewController: UIViewController {
    let db = Firestore.firestore()
    var hr: NSDictionary = NSDictionary()
    var hrEntries : [Int] = []
    var dayEntries: [String] = []
    var postsText: [Message] = []
    var userName = ""
    var ID = ""
    var flag = 0
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var messageTextField: UITextField!
    
    @IBOutlet weak var nameLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        nameLabel.text = userName
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(UINib(nibName: "PostCell", bundle: nil ), forCellReuseIdentifier: "post")
        loadPosts()
    }
    
    @IBAction func viewPatientStatus(_ sender: UIButton) {
        var userID = ""
        if flag == 1 {
            userID = self.ID
            let docRef = db.collection("users").document(userID)
        }
        else {
            userID = (Auth.auth().currentUser?.uid)!
            
        }
        
        let docRef = db.collection("users").document(userID)
        
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                let dataDescription = document.data()
                
                self.hr = (dataDescription?["hourlyHR"] as? NSDictionary)!
                //                print("Document data: \(hr["Friday"]!)")
                //                print(self.hr.count)
                let HRCount = self.hr.count
                //                print(self.hr )
                for (key, value) in self.hr {
                    //                    print("Value: \(value) for key: \(key)")
                    self.hrEntries.append(value as! Int)
                    self.dayEntries.append(key as! String)
                    
                }
                
                self.performSegue(withIdentifier: "patientStatus", sender: self)
                
            }
            else
            {
                print("Document does not exist")
            }
        }
        
        
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "patientStatus" {
            let destinationVC = segue.destination as! PatientStatusViewController
            destinationVC.dayEntries = dayEntries
            destinationVC.hrEntries = hrEntries
            
        }
    }
    
    
    func loadPosts(){
        
        db.collection("users").document("TzV04aBQlhTn8CN7w07Ub1p9k7G2").collection("posts").order(by: "date",descending: true).addSnapshotListener {(querySnapshot , error ) in if let e = error {
            print("There was an issue retrieving from  Firestore , \(e)" )
        }
            
        else {
            self.postsText = []
            if let queryDocs = querySnapshot?.documents{
                for doc in queryDocs    //looping on all the documents in the collection
                {
                    
                    let data = doc.data()
                    if let mesgSender = data["uidSender"] as? String , let messageBody = data["message"] as? String,
                        let receiver = data["nameReceiver"] as? String,
                        let name = data["nameSender"] as? String {
                        let newMsg = Message(messageSender: name, message: messageBody,uidSender: mesgSender, messageReceiver: receiver  )
                        self.postsText.append(newMsg)   //saving the messages in a dictionary
                        
                        //------ to reload table view every new message and get view to the last message for better user experience
                        DispatchQueue.main.async {
                            self.tableView.reloadData()
                            let indexPath = IndexPath(row: self.postsText.count-1 , section: 0)
                            self.tableView.scrollToRow(at: indexPath, at: .top, animated: false)
                        }
                        //---------
                    }
                }
            }
            }
            
        }
        
    }
    
    
    @IBAction func postPressed(_ sender: UIButton) {
        var senderName = ""
        let userID = Auth.auth().currentUser?.uid
        let docRef = db.collection("users").document(userID!)
        if flag == 1{
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                let dataDescription = document.data()
                //        print("Document data: \((dataDescription?["name"])!)")
                senderName = (dataDescription?["name"] as? String)!
                
                if let messageBody = self.messageTextField.text , let messageSender = userID {
                    self.db.collection("users").document("TzV04aBQlhTn8CN7w07Ub1p9k7G2").collection("posts").addDocument(data: [
                        "uidSender": messageSender,"message": messageBody , "nameSender": senderName ,"nameReceiver": "Nouran Flaifel" , "date": Date()
                    ]){
                        (error) in if let e=error {
                            print("There was an issue saving post to Firestore , \(e)" )
                        }
                        else {
                            print("Saved successfully posts to firestore" )
                        }
                    }
                }
                self.messageTextField.text = ""
                
                
            }
            else {
                print("Document does not exist")
            }
            
        }
        
        
    }
        else{
            docRef.getDocument { (document, error) in
                if let document = document, document.exists {
                    let dataDescription = document.data()
                    //        print("Document data: \((dataDescription?["name"])!)")
                    senderName = (dataDescription?["name"] as? String)!
                    
                    if let messageBody = self.messageTextField.text , let messageSender = userID {
                        self.db.collection("users").document("TzV04aBQlhTn8CN7w07Ub1p9k7G2").collection("posts").addDocument(data: [
                            "uidSender": messageSender,"message": messageBody , "nameSender": senderName ,"nameReceiver": senderName , "date": Date()
                        ]){
                            (error) in if let e=error {
                                print("There was an issue saving post to Firestore , \(e)" )
                            }
                            else {
                                print("Saved successfully posts to firestore" )
                            }
                        }
                    }
                    self.messageTextField.text = ""
                    
                    
                }
                else {
                    print("Document does not exist")
                }
                
            }
            
            
        }
    }
}


extension PatientProfileViewController: UITableViewDataSource , UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return postsText.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var nameSenderReceiver = ""
        let cell = tableView.dequeueReusableCell(withIdentifier: "post" , for: indexPath) as! PostCell
        let post = postsText[indexPath.row]
        //        cell.name.text = requestNames[indexPath.row]
        cell.postBody.text = post.message
        if post.messageSender == post.messageReceiver {
        nameSenderReceiver = post.messageSender
        }
        else{
         nameSenderReceiver = post.messageSender + " ► " + post.messageReceiver
        }
        cell.name.text = nameSenderReceiver
        
        return cell
        
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        return 80 // the height for custom cell 0
        
    }
    
}
