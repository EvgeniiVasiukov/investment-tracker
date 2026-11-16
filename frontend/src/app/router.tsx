import { createBrowserRouter } from 'react-router'
import { AppLayout } from './AppLayout'
import {DashBoardPage} from "../pages/DashBoardPage.tsx";
import {PortfolioPage} from "../pages/PortfolioPage.tsx";
import {CreditsPage} from "../pages/CreditsPage.tsx";
import {NotFoundPage} from "../pages/NotFoundPage.tsx";
import {LoginPage} from "@/pages/LoginPage.tsx";
import {ProtectedRoute} from "@/features/auth/ProtectedRoute.tsx";

export const router = createBrowserRouter([
    {
        path : 'login',
        element : <LoginPage />,
}, {element: <ProtectedRoute />,
        children: [
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
        ]},],},
])