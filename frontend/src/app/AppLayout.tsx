import {Outlet, useNavigate} from 'react-router'
import { Sidebar } from '../components/Sidebar'
import { useAuth } from '../features/auth/useAuth'
import { Button } from '../components/ui/button'
import './AppLayout.css'

export function AppLayout() {
    const navigate = useNavigate();
    const {logout } = useAuth()
    function handleLogout() {
        logout()
        navigate('/login', {replace:true})
    }
    return (
        <div className="app-layout">
            <header className="app-header">
                <span>Investment Tracker</span>

            <Button variant="outline" onClick={handleLogout}>
                Log Out
            </Button>
        </header>

            <aside className="app-sidebar">
                <Sidebar />
            </aside>

            <main className="app-content">
                <Outlet />
            </main>
        </div>
    )
}