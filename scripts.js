// Datos simulados de cartas
const letters = [
    {
        id: 1,
        becario: "Juan Pérez",
        padrino: "Ana Gómez",
        mediador: "Carlos López",
        estado: "Subida",
        fecha: "2025-06-01",
        assignedTo: null,
        files: ["carta1.pdf"],
        observations: [],
    },
    {
        id: 2,
        becario: "María Rodríguez",
        padrino: "Luis Martínez",
        mediador: "Sofía Torres",
        estado: "Asignada",
        fecha: "2025-06-02",
        assignedTo: "voluntario1@correo.com",
        files: ["carta2.jpg", "carta2b.png"],
        observations: [
            {
                user: "voluntario1",
                text: "Falta firma",
                timestamp: "2025-06-02 10:00",
            },
        ],
    },
];

// Función para mostrar mensaje de error
function showError(message) {
    alert(message);
}

// Función para simular descarga
function downloadLetter(id) {
    console.log(`Descargando carta con ID: ${id}`);
    alert(`Carta ${id} descargada`);
}

// Función para mostrar detalles
function showDetails(id) {
    const letter = letters.find((l) => l.id === id);
    alert(
        `Detalles de la carta ${id}:\nBecario: ${letter.becario}\nPadrino: ${
            letter.padrino
        }\nMediador: ${letter.mediador}\nEstado: ${letter.estado}\nFecha: ${
            letter.fecha
        }\nAsignada a: ${letter.assignedTo || "Nadie"}`
    );
}
