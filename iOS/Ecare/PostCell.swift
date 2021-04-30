//
//  PostCell.swift
//  Ecare
//
//  Created by Nouran Adel on 29/03/2021.
//  Copyright © 2021 Nouran Adel. All rights reserved.
//

import UIKit

class PostCell: UITableViewCell {

    @IBOutlet weak var postBody: UILabel!
    @IBOutlet weak var name: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
