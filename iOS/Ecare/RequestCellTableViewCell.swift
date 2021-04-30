//
//  RequestCellTableViewCell.swift
//  Ecare
//
//  Created by Nouran Adel on 01/03/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase

class RequestCellTableViewCell: UITableViewCell {
    
    @IBOutlet weak var sender: UILabel!
    var uid = ""
    
    let db = Firestore.firestore()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    @IBAction func acceptButton(_ sender: Any) {
        print("accept pressed")
        let userID = Auth.auth().currentUser?.uid
        let docRef = db.collection("users").document(userID!).collection("requests")
        docRef.getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                if let queryDocs = querySnapshot?.documents{
                    for doc in queryDocs    //looping on all the documents in the collection
                    {
                        let data = doc.data()
                        if let who = data["status"] as? Int{
                            
                            if who == 0 && data["requestUid"]! as! String == self.uid{
                                docRef.document(doc["requestUid"] as! String).setData(["requestUid": doc["requestUid"] as! String, "status": 1]) {
                                    (error) in if let e=error {
                                        print("There was an issue saving to Firestore , \(e)" )
                                    }
                                    else {
                                        print("Accepted successfully to firestore" )
                                             self.db.collection("users").document(data["requestUid"]! as! String).collection("requests").document(userID!).setData([ "requestUid": userID!, "status": 1
                                                                                                                                        
                                                         ]){
                                                             (error) in if let e=error {
                                                                 print("There was an issue saving to Firestore , \(e)" )
                                                             }
                                                             else {
                                                                 print("Created 'requests' at caregiver too in database" )

                                                             }
                                                         }
                                        
                                        
                                    }
                                }
                                
                            }
                        }
                    }
                } //--for
            }
            
            
        }
    }
    
    
    
    @IBAction func declineButton(_ sender: UIButton) {
    }
}
