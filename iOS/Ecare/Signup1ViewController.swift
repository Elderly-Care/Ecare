//
//  Signup1ViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 10/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit

class Signup1ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func signUpCaregiver(_ sender: UIButton) {
        self.performSegue(withIdentifier: "caregiver", sender: self)
    }
    
    @IBAction func signUpPatient(_ sender: UIButton) {
                self.performSegue(withIdentifier: "patient", sender: self)
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
