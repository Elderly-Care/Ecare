//
//  ReportsViewController.swift
//  Ecare
//
//  Created by Nouran Adel on 23/03/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit

class ReportsViewController: UIViewController {
    var which1 = ""
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    @IBAction func heartRate(_ sender: UIButton) {
        
        which1 = "heart"
        self.performSegue(withIdentifier: "data", sender: self)
    }
    
    @IBAction func steps(_ sender: UIButton) {
        
        which1 = "steps"
        self.performSegue(withIdentifier: "data", sender: self)
    }
    
    @IBAction func calories(_ sender: UIButton) {
        which1 = "calories"
        self.performSegue(withIdentifier: "data", sender: self)
    }
    
    @IBAction func sleep(_ sender: UIButton) {
        which1 = "sleep"
        self.performSegue(withIdentifier: "data", sender: self)
    }
    
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "data" {
            let destinationVC = segue.destination as! DataViewController
            destinationVC.whichOne = which1
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
