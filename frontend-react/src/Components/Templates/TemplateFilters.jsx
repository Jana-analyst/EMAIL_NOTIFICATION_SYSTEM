import { Input, Select, Space } from "antd";

const { Option } = Select;

function TemplateFilters({ searchValue, onSearchChange, sortValue, onSortChange }) {
  return (
    <Space style={{ marginBottom: 16 }}>
      <Input
        placeholder="Search templates"
        value={searchValue}
        onChange={(e) => onSearchChange(e.target.value)}
        style={{ width: 220 }}
      />

      <Select
        value={sortValue}
        onChange={onSortChange}
        style={{ width: 180 }}
      >
        <Option value="newest">Newest First</Option>
        <Option value="oldest">Oldest First</Option>
      </Select>
    </Space>
  );
}

export default TemplateFilters;