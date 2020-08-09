import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'app-init-download-button',
    templateUrl: './init-download-button.component.html',
    styleUrls: ['./init-download-button.component.css']
})
export class InitDownloadButtonComponent {

    @Input() public initInProgress: boolean;
    @Input() public disabled: boolean;
    @Input() public buttonClass: string = '';
    @Output() public init = new EventEmitter<void>();

    constructor() {
    }

    public onInit() {
        this.init.emit();
    }

}