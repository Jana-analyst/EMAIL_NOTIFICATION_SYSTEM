import Navigation from "./Navigation";

function Sidebar() {
  return (
    <div style={{ height: "100%", display: "flex", flexDirection: "column" }}>
      <div
        style={{
          height: 64,
          display: "flex",
          alignItems: "center",
          paddingLeft: 16,
          fontWeight: 600,
          borderBottom: "1px solid #f0f0f0",
        }}
      >
        ENS
      </div>

      <div style={{ flex: 1, paddingTop: 8 }}>
        <Navigation />
      </div>
    </div>
  );
}

export default Sidebar;