import { Outlet } from 'react-router'
import { Sidebar } from '../components/Sidebar'
import './AppLayout.css'

export function AppLayout() {
    return (
        <div className="app-layout">
            <header className="app-header">
                Investment Tracker
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