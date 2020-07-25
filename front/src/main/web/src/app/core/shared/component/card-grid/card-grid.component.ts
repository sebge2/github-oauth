import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
    selector: 'app-card-grid',
    templateUrl: './card-grid.component.html',
    styleUrls: ['./card-grid.component.css']
})
export class CardGridComponent implements OnInit {

    @Output() public add = new EventEmitter<void>();

    constructor() {
    }

    ngOnInit() {
    }

    public onAdd() {
        this.add.emit();
    }
}