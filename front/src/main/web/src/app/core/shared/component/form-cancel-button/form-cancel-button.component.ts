import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
    selector: 'app-form-cancel-button',
    templateUrl: './form-cancel-button.component.html',
    styleUrls: ['./form-cancel-button.component.css']
})
export class FormCancelButtonComponent implements OnInit {

    @Input() public form: FormGroup;
    @Input() public loading: boolean;
    @Output() public reset = new EventEmitter<void>();

    constructor() {
    }

    ngOnInit() {
    }

    public onReset() {
        this.reset.emit();
    }
}
