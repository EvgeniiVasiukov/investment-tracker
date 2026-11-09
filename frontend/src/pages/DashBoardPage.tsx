import { useEffect, useState } from 'react'
import { httpClient } from '../api/httpClient'

interface HealthResponse {
    status: string
}

export function DashBoardPage() {
    const [status, setStatus] = useState('Checking...')

    useEffect(() => {
        httpClient
            .get<HealthResponse>('/actuator/health')
            .then((data) => setStatus(data.status))
            .catch((error: unknown) => {
                console.error(error)
                setStatus('Connection failed')
            })
    }, [])

    return (
        <div>
            <h1>Dashboard</h1>
            <p>API Gateway: {status}</p>
        </div>
    )
}