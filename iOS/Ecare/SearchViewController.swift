//
//  SearchViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 12/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase
class SearchViewController: UIViewController {
    
    @IBOutlet weak var table: UITableView!
    @IBOutlet weak var searchBar: UISearchBar!
    
    
    let db = Firestore.firestore()
    var names: [String] = []
    var emails: [String] = []
    var namescopy: [String] = []
    var uid: [String] = []
    var uidSelected = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        loadUsers()
        table.delegate = self
        table.dataSource = self
        self.title = "Add Patients"
        
        
    }
    
    func loadUsers() {
        db.collection("users").getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                if let queryDocs = querySnapshot?.documents{
                    for doc in queryDocs    //looping on all the documents in the collection
                    {
                        let data = doc.data()
                        if let who = data["registerType"] as? String{
                            
                            if who == "Patient"{
                                
                                if let searchNames = data["name"] as? String,  let searchEmails = data["email"] as? String, let searchUID = data["uid"] as? String {
                                    self.names.append(searchNames)
                                    self.emails.append(searchEmails)
                                    self.uid.append(searchUID)
                                    
                                    DispatchQueue.main.async {
                                        self.table.reloadData()
                                        let indexPath = IndexPath(row: self.names.count-1 , section: 0)
                                        self.table.scrollToRow(at: indexPath, at: .top, animated: false)
                                    }
                                }
                            }
                        }
                    } //--for
                }
                
                
            }
        }
    }
    
}




extension SearchViewController: UITableViewDataSource , UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return names.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = self.table.dequeueReusableCell(withIdentifier: "cell" , for: indexPath)
        
        cell.textLabel?.text = names[indexPath.row]
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let emailSelected = emails[indexPath.row]
        uidSelected = uid[indexPath.row]
        self.performSegue(withIdentifier: "searchSelect", sender: self)
        
        
        
        //        print(emailSelected)
        //        print(uidSelected)
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "searchSelect" {
            let destinationVC = segue.destination as! SearchProfileViewController
            destinationVC.uid = uidSelected
        }
    }
    
}





