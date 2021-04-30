//
//  CaregiverProfileViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 16/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit

class CaregiverProfileViewController: UIViewController {
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var emailLabel: UILabel!
    @IBOutlet weak var phonenumberLabel: UILabel!
    @IBOutlet weak var relationLabel: UILabel!
    var userName = ""
    var phoneNumber = ""
    var relation = ""
    var email = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        nameLabel.text = userName
        phonenumberLabel.text = phoneNumber
        relationLabel.text = relation
        emailLabel.text = email
        // Do any additional setup after loading the view.
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
