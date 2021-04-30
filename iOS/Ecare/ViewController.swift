//
//  ViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 08/02/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit
import Firebase


class ViewController: UIViewController {
    @IBOutlet weak var email: UITextField!
    @IBOutlet weak var password: UITextField!
    
      override func viewWillAppear(_ animated: Bool) {
    super.viewWillAppear(animated)
        navigationController?.isNavigationBarHidden = true
    }
    
    override func viewWillDisappear(_ animated: Bool) {
          super.viewWillDisappear(animated)
          navigationController?.isNavigationBarHidden = false
      }
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }

    @IBAction func signin(_ sender: UIButton) {
        if let user_email = email.text , let user_password = password.text{
            Auth.auth().signIn(withEmail: user_email, password: user_password) { [weak self] authResult, error in
                guard let self = self else { return }
                
                if let e = error {
                    
                    print(e.localizedDescription)
                    
                }
                else {
                    self.performSegue(withIdentifier: "patientMain", sender: self)
                }
            }
        }
        
        
    }
    
    @IBAction func signup(_ sender: UIButton) {
        
        self.performSegue(withIdentifier: "signup", sender: self)
    }
}

