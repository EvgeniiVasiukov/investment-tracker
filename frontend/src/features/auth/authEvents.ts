type UnauthorizedListener = () => void

const listeners = new Set<UnauthorizedListener>()

export const authEvents = {
    subscribe(listener: UnauthorizedListener): () => void {
        listeners.add(listener)

        return () => {
            listeners.delete(listener)
        }
    },

    notifyUnauthorized(): void {
        listeners.forEach((listener) => listener())
    },
}