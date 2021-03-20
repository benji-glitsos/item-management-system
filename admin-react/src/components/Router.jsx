import { Router, Route, Switch } from "react-router";
import { createBrowserHistory } from "history";
import Page from "%/presenters/PagePresenter";
import ReadmePage from "%/components/ReadmePage";
import UsersPage from "%/components/UsersPage";

export default () => (
    <Router history={createBrowserHistory()}>
        <Page>
            <Switch>
                <Route exact path="/" component={ReadmePage} />
                <Route path="/users" component={UsersPage} />
            </Switch>
        </Page>
    </Router>
);
