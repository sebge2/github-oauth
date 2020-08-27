import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule, Routes} from "@angular/router";
import {CoreSharedModule} from "../core/shared/core-shared-module";
import {CoreAuthModule} from "../core/auth/core-auth.module";
import {UsersComponent} from './component/users/users.component';
import {RepositoriesComponent} from './component/repositories/repositories.component';
import {LocalesComponent} from './component/locales/locales.component';
import {LocaleViewCardComponent} from './component/locales/locale-view-card/locale-view-card.component';
import {RepositoryViewCardComponent} from './component/repositories/repository-list/repository-view-card/repository-view-card.component';
import {UserViewCardComponent} from './component/users/user-view-card/user-view-card.component';
import {RepositoryDetailsComponent} from './component/repositories/repository-details/repository-details.component';
import {RepositoryListComponent} from './component/repositories/repository-list/repository-list.component';
import {RepositoryAddWizardComponent} from './component/repositories/repository-list/repository-add-wizard/repository-add-wizard.component';
import {RepositoryAddWizardStepTypeComponent} from './component/repositories/repository-list/repository-add-wizard/repository-add-wizard-step-type/repository-add-wizard-step-type.component';
import {RepositoryAddWizardStepInfoComponent} from './component/repositories/repository-list/repository-add-wizard/repository-add-wizard-step-info/repository-add-wizard-step-info.component';
import {RepositoryAddWizardStepCreationComponent} from './component/repositories/repository-list/repository-add-wizard/repository-add-wizard-step-creation/repository-add-wizard-step-creation.component';
import {RepositoryAddWizardStepInitializationComponent} from './component/repositories/repository-list/repository-add-wizard/repository-add-wizard-step-initialization/repository-add-wizard-step-initialization.component';
import { RepositoryDetailsConfigComponent } from './component/repositories/repository-details/repository-details-config/repository-details-config.component';
import { RepositoryDetailsWorkspacesComponent } from './component/repositories/repository-details/repository-details-workspaces/repository-details-workspaces.component';
import { RepositoryDetailsWorkspaceNodeComponent } from './component/repositories/repository-details/repository-details-workspaces/repository-details-workspace-node/repository-details-workspace-node.component';
import { RepositoryDetailsBundleFileNodeComponent } from './component/repositories/repository-details/repository-details-workspaces/repository-details-bundle-file-node/repository-details-bundle-file-node.component';
import { RepositoryDetailsBundleFileEntryNodeComponent } from './component/repositories/repository-details/repository-details-workspaces/repository-details-bundle-file-entry-node/repository-details-bundle-file-entry-node.component';
import { RepositoryDetailsWorkspaceTreeNodeComponent } from './component/repositories/repository-details/repository-details-workspaces/repository-details-workspace-tree-node/repository-details-workspace-tree-node.component';

const appRoutes: Routes = [
    {path: 'users', component: UsersComponent},
    {path: 'repositories', component: RepositoriesComponent},
    {path: 'locales', component: LocalesComponent}
];

@NgModule({
    declarations: [
        LocalesComponent,
        LocaleViewCardComponent,

        UsersComponent,
        UserViewCardComponent,

        RepositoriesComponent,
        RepositoryListComponent,
        RepositoryViewCardComponent,
        RepositoryAddWizardComponent,
        RepositoryAddWizardStepTypeComponent,
        RepositoryAddWizardStepInfoComponent,
        RepositoryAddWizardStepCreationComponent,
        RepositoryAddWizardStepInitializationComponent,
        RepositoryDetailsComponent,
        RepositoryDetailsConfigComponent,
        RepositoryDetailsWorkspacesComponent,
        RepositoryDetailsWorkspaceNodeComponent,
        RepositoryDetailsBundleFileNodeComponent,
        RepositoryDetailsBundleFileEntryNodeComponent,
        RepositoryDetailsWorkspaceTreeNodeComponent
    ],
    entryComponents: [
        RepositoryAddWizardComponent
    ],
    imports: [
        CommonModule,
        CoreSharedModule,
        CoreAuthModule,

        RouterModule.forChild(appRoutes)
    ],
    exports: [RouterModule]
})
export class AdminModule {
}
