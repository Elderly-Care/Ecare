//
//  RequestsViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 16/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase



class RequestsViewController: UIViewController {
    
    @IBOutlet weak var requestTable: UITableView!
    
    let db = Firestore.firestore()
    var requestNames: [String] = []
    var requestUid: [String] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.requestTable.rowHeight = 44;
        loadRequests()
        requestTable.delegate = self
        requestTable.dataSource = self
        requestTable.register(UINib(nibName: "RequestCellTableViewCell", bundle: nil ), forCellReuseIdentifier: "requestCell")
        
        // Do any additional setup after loading the view.
    }
    
    func loadRequests() {
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
                                    print("Document data: \((data))")
                                    if data["status"] as? Int == 0{
                                        let name = (dataDescription?["name"] as? String)!
                                        
                                        
                                        self.requestNames.append(name)
                                        
                                        self.requestUid.append(requestUID!)
                                        print("requests are:\(self.requestNames)")
                                    }

                                    DispatchQueue.main.async {
                                        self.requestTable.reloadData()
                                        let indexPath = IndexPath(row: self.requestNames.count-1 , section: 0)
//                                      self.requestTable.scrollToRow(at: indexPath, at: .top, animated: false)
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
    
    
    /*
     // MARK: - Navigation
     
     // In a storyboard-based application, you will often want to do a little preparation before navigation
     override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
     // Get the new view controller using segue.destination.
     // Pass the selected object to the new view controller.
     }
     */
    
}

extension RequestsViewController: UITableViewDataSource , UITableViewDelegate {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return requestNames.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        //        let cell = self.requestTable.dequeueReusableCell(withIdentifier: "cell" , for: indexPath)
        let cell = requestTable.dequeueReusableCell(withIdentifier: "requestCell" , for: indexPath) as! RequestCellTableViewCell
        
        cell.sender.text = requestNames[indexPath.row]
        cell.uid = requestUid[indexPath.row]
        
        
        //        cell.textLabel?.text = requestNames[indexPath.row]
        return cell
        
    }
    
}


