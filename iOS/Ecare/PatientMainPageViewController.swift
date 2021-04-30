//
//  PatientMainPageViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 10/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class PatientMainPageViewController: UIViewController {
    let db = Firestore.firestore()
    var requestNames: [String] = []
    var requestUid: [String] = []
    var userName = ""
    var who = ""
    var phoneNumber = ""
    var relation = ""
    var email = ""
    var requestN = ""
    var requestID = ""
    
    
    @IBOutlet weak var table: UITableView!
    @IBOutlet weak var search: UIButton!
    @IBOutlet weak var emergencyButton: UIButton!
    @IBOutlet weak var searchButton: UIButton!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var requests: UIButton!
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        let userID = Auth.auth().currentUser?.uid
        let docRef = db.collection("users").document(userID!)
        
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                let dataDescription = document.data()
                if let registerWho = dataDescription?["registerType"] as? String {
                    if registerWho == "Patient" {
                        self.searchButton.isHidden = true
                        self.emergencyButton.isHidden = false
                        self.requests.isHidden = false
                    }
                    else {
                        self.emergencyButton.isHidden = true
                        self.searchButton.isHidden = false
                        self.requests.isHidden = true
                        self.table.delegate = self
                        self.table.dataSource = self
                    }
                }
                
            } else {
                print("Document does not exist")
            }
        }
        
        
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        table.delegate = self
        table.dataSource = self
        search.layer.cornerRadius = search.frame.width / 2
        search.layer.masksToBounds = true
        requests.layer.cornerRadius = search.frame.width / 2
        requests.layer.masksToBounds = true
        loadcaregivers()
        loadInfo()
        
        // Do any additional setup after loading the view.
    }
    
    @IBAction func search(_ sender: UIButton) {
        self.performSegue(withIdentifier: "search", sender: self)
    }
    
    @IBAction func signout(_ sender: UIBarButtonItem) {
        do {
            try Auth.auth().signOut()
            navigationController?.popToRootViewController(animated: true)
        } catch let signOutError as NSError {
            print ("Error signing out: %@", signOutError)
        }
    }
    
    func loadInfo(){
        let userID = Auth.auth().currentUser?.uid
        let docRef = db.collection("users").document(userID!)
        
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                let dataDescription = document.data()
                //        print("Document data: \((dataDescription?["name"])!)")
                self.userName = (dataDescription?["name"] as? String)!
                self.nameLabel.text = self.userName
                self.who = (dataDescription?["registerType"] as? String)!
                self.phoneNumber = (dataDescription?["phone"] as? String)!
                self.email = (dataDescription?["email"] as? String)!
                if self.who == "Caregiver"{
                    self.relation = (dataDescription?["relation"] as? String)!
                }
                
            }
            else {
                print("Document does not exist")
            }
            
        }
        
    }
    func loadcaregivers(){
        let myUID = Auth.auth().currentUser?.uid
        
        db.collection("users").document(myUID!).collection("requests").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                if let queryDocs = querySnapshot?.documents{
                    for doc in queryDocs    //looping on all the documents in the collection
                    {
                        let data = doc.data()
                        let requestUID = data["requestUid"] as? String
                        let docRef = self.db.collection("users").document(requestUID!)
                        docRef.getDocument { (document, error) in
                            if let document = document, document.exists {
                                let dataDescription = document.data()
//                                print("Document data: \((data))")
                                if data["status"] as? Int == 1{
                                    let name = (dataDescription?["name"] as? String)!
                                    
                                    
                                    self.requestNames.append(name)
                                    
                                    self.requestUid.append(requestUID!)
//                                    print(self.requestNames)
                                }
                                
                                DispatchQueue.main.async {
                                    self.table.reloadData()
                                    let indexPath = IndexPath(row: self.requestNames.count-1 , section: 0)
//                                  self.table.scrollToRow(at: indexPath, at: .top, animated: false)
                                }
                            }
                                
                            else {
                                print("Document does not exist")
                            }
                        }
                        
                        
                        
                        
                    } //--for
                }
            }
        }
    }
    
    
    
    @IBAction func viewProfile(_ sender: UIButton) {
        if who == "Patient"{
            self.performSegue(withIdentifier: "patientProfile", sender: self)
        }
        else{
            self.performSegue(withIdentifier: "caregiverProfile", sender: self)
        }
    }
    

    
    @IBAction func requests(_ sender: UIButton) {
        self.performSegue(withIdentifier: "requests", sender: self)
    }
    
    
    
}
extension PatientMainPageViewController: UITableViewDataSource , UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return requestNames.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = self.table.dequeueReusableCell(withIdentifier: "cell" , for: indexPath)
        
        cell.textLabel?.text = requestNames[indexPath.row]
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        requestN = requestNames[indexPath.row]
        requestID = requestUid[indexPath.row]
        if  who == "Patient"{
        self.performSegue(withIdentifier: "existingCaregiver", sender: self)
        }
        else {
            self.performSegue(withIdentifier: "existingPatient", sender: self)
        }
        
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "patientProfile" {
            let destinationVC = segue.destination as! PatientProfileViewController
            destinationVC.userName = userName
        }
        else if segue.identifier == "caregiverProfile" {
            let destinationVC = segue.destination as! CaregiverProfileViewController
            destinationVC.userName = userName
            destinationVC.relation = relation
            destinationVC.phoneNumber = phoneNumber
            destinationVC.email = email
        }
        else if segue.identifier == "existingPatient" {
            let destinationVC = segue.destination as! PatientProfileViewController
            destinationVC.userName = requestN
            destinationVC.ID = requestID
            destinationVC.flag = 1
        }
        else if segue.identifier == "existingCaregiver" {
            let destinationVC = segue.destination as! CaregiverEmergencyViewController
            destinationVC.userName = requestN
            destinationVC.ID = requestID
        }
    }
}

