import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {AdminComponent} from './component/admin/admin.component';
import { WorkspaceTableComponent } from './component/workspace-table/workspace-table.component';
import {MaterialModule} from "../core/ui/material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { RepositoryInitializerComponent } from './component/repository-initializer/repository-initializer.component';

const appRoutes: Routes = [
    {path: '', pathMatch: 'full', component: AdminComponent}
];

@NgModule({
    declarations: [AdminComponent, WorkspaceTableComponent, RepositoryInitializerComponent],
    imports: [
        CommonModule,
        RouterModule.forChild(appRoutes),
        MaterialModule,
        ReactiveFormsModule,
        FormsModule
    ],
    exports: [RouterModule]
})
export class AdminModule {
}