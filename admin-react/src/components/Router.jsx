import { Suspense, lazy } from "react";
import { Router, Route, Switch } from "react-router";
import { createBrowserHistory } from "history";
import { QueryParamProvider } from "use-query-params";
import Analytics from "react-router-ga";
import Page from "%/components/Page/PagePresenter";
import LoadingPage from "%/components/LoadingPage";

const ReadmePage = lazy(() => import("%/components/ReadmePage"));
const UsersListPage = lazy(() => import("%/components/UsersListPage"));
const UsersOpenPage = lazy(() => import("%/components/UsersOpenPage"));
const ItemsListPage = lazy(() => import("%/components/ItemsListPage"));
const NotFoundPage = lazy(() => import("%/components/NotFoundPage"));

export default () => (
    <Router history={createBrowserHistory()}>
        <Analytics id={process.env.REACT_APP_PROJECT_GOOGLE_ANALYTICS_ID}>
            <QueryParamProvider>
                <Page>
                    <Suspense fallback={<LoadingPage />}>
                        <Switch>
                            <Route exact path="/">
                                <ReadmePage />
                            </Route>
                            <Route exact path="/users">
                                <UsersListPage />
                            </Route>
                            <Route path="/users/:username">
                                <UsersOpenPage />
                            </Route>
                            <Route path="/items">
                                <ItemsListPage />
                            </Route>
                            <Route path="*">
                                <NotFoundPage />
                            </Route>
                        </Switch>
                    </Suspense>
                </Page>
            </QueryParamProvider>
        </Analytics>
    </Router>
);
