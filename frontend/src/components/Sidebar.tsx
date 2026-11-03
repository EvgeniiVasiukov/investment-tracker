import { NavLink } from 'react-router'
import './Sidebar.css'

export function Sidebar() {
    return (
        <nav className="sidebar-nav">
            <NavLink to="/" end className={({ isActive }) => isActive ? 'active' : ''}>
                Dashboard
            </NavLink>

            <NavLink to="/portfolio" className={({ isActive }) => isActive ? 'active' : ''}>
                Portfolio
            </NavLink>

            <NavLink to="/credits" className={({ isActive }) => isActive ? 'active' : ''}>
                Credits
            </NavLink>
        </nav>
    )
}