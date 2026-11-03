import { createBrowserRouter } from 'react-router'
import { AppLayout } from './AppLayout'
import {DashBoardPage} from "../pages/DashBoardPage.tsx";
import {PortfolioPage} from "../pages/PortfolioPage.tsx";
import {CreditsPage} from "../pages/CreditsPage.tsx";
import {NotFoundPage} from "../pages/NotFoundPage.tsx";

export const router = createBrowserRouter([
    {
        path: '/',
        element: <AppLayout />,
        children: [
            {
                index: true,
                element: <DashBoardPage />,
            },
            {
                path: 'portfolio',
                element: <PortfolioPage />,
            },
            {
                path: 'credits',
                element: <CreditsPage />,
            },
            {
                path: '*',
                element: <NotFoundPage />,
            }
        ],
    },
])