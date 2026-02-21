<?php
header('Content-Type: application/json');
require '../config/db.php';

$result = $conn->query("SELECT id, name FROM categories ORDER BY id ASC");

$categories = [];
while ($row = $result->fetch_assoc()) {
    $row['id'] = (int)$row['id'];
    $categories[] = $row;
}

echo json_encode(['status' => 'success', 'categories' => $categories]);
$conn->close();
