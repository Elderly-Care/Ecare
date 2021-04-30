//
//  LocationHistMainViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 04/04/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit

class LocationHistMainViewController: UIViewController {
    var which = ""
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func kitchenPressed(_ sender: UIButton) {
        self.which = "kitchen"
        self.performSegue(withIdentifier: "bathroomHistory", sender: self)
    }
    
    @IBAction func bathroomPressed(_ sender: UIButton) {
        self.which = "bathroom"
         self.performSegue(withIdentifier: "bathroomHistory", sender: self)
        
    }
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "bathroomHistory" {
            let destinationVC = segue.destination as! LocationHistoryViewController
            destinationVC.which = which
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
